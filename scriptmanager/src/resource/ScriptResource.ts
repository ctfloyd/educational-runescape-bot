import express, { response, Router } from 'express';
import { ScriptManagerService } from '../service/ScriptManagerService';
import { ScriptRoutes, ApiRunescapeAccount, isApiScriptStartRequest, ApiScriptStartRequest, RunescapeAccount, ScriptStartRequest } from '../interfaces/interfaces';

export class ScriptResource {

    public static PREFERRED_MOUNTING_LOCATION = '/scripts';
    private static readonly routes: ScriptRoutes = {
        all: '/all',
        accounts: '/accounts',
        start: '/start',
        end: '/end',
        current: '/current',
        stdout: '/stdout',
        stderr: '/stderr'
    }

    private scriptManagerService: ScriptManagerService;
    private router: Router | undefined;

    constructor(scriptManagerSerivce : ScriptManagerService) {
        if (!scriptManagerSerivce) {
            throw new Error('ScriptManagerService is required.');
        }

        this.scriptManagerService = scriptManagerSerivce;
    }
    
    public initialize(): void {
        this.router = Router();
        this.router.use(express.json());

        this.router.get(ScriptResource.routes.all, this.getAllAvailableScripts);
        this.router.get(ScriptResource.routes.accounts, this.getAllAccounts);
        this.router.post(ScriptResource.routes.start, this.startScript);
        this.router.post(ScriptResource.routes.end, this.endScripts);
        this.router.get(ScriptResource.routes.current, this.getCurrentScript);
        this.router.get(ScriptResource.routes.stdout, this.getStdOut);
        this.router.get(ScriptResource.routes.stderr, this.getStdErr);
    }

    public getRouter(): Router | undefined {
        return this.router;
    }

    public async getAllAvailableScripts(_: express.Request, response: express.Response) : Promise<void> {
        response.status(200).json({
            scripts: await this.scriptManagerService.getAllScripts()
        });
    }

    public async getAllAccounts(_: express.Request, response: express.Response) : Promise<void> {
        const runescapeAccounts = await this.scriptManagerService.getAllAccounts();
        const apiRunescapeAccounts: ApiRunescapeAccount[] = runescapeAccounts.map(account => ({ email: account.email }));

        response.status(200).json({
            accounts: apiRunescapeAccounts
        });
    }

    public async startScript(request: express.Request, response: express.Response) : Promise<void> {
        if(!isApiScriptStartRequest(request.body)) {
            response.status(400).json({
                message: 'Malformed script start request'
            });

            return;
        }

        const apiScriptStartRequest = request.body as ApiScriptStartRequest;
        const account = await this.resolveRunescapeAccountFromEmail(apiScriptStartRequest.account.email);
        
        if (!account) {
            response.status(404).json({
                message: `Could not find account credentials matching email (${apiScriptStartRequest.account.email})`
            });

            return;
        }

        const scriptStartRequest: ScriptStartRequest = {
            script_name: apiScriptStartRequest.script_name,
            script_parameters: apiScriptStartRequest.script_parameters,
            account: account
        }

        try {
            this.scriptManagerService.start(scriptStartRequest);
            response.status(200).send();

        } catch (e) {
            response.status(400).json({
                message: e
            });
        }
    }

    public async endScripts(_: express.Request, response: express.Response) : Promise<void> {
        await this.scriptManagerService.end();
        response.status(200).send();
    }

    public getStdOut() {
        response.status(200).json({
            stdout: this.scriptManagerService.getStdOut()
        });
    }

    public getStdErr() {
        response.status(200).json({
            stderr: this.scriptManagerService.getStdErr()
        });
    }

    public getCurrentScript() {
        response.status(200).json({
            current_script: this.scriptManagerService.getCurrentScript()
        });
    }

    private async resolveRunescapeAccountFromEmail(email: string) : Promise<RunescapeAccount | null> {
        const accounts = await this.scriptManagerService.getAllAccounts()
        for (const account of accounts) {
            if (account.email === email) {
                return account;
            }
        }

        return null;
    }
    
}
