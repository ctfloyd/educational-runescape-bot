import { homedir } from 'os';
import fs from 'fs';
import { ScriptStartRequest, Account, RunescapeAccount } from '../interfaces/interfaces';
import { spawn, ChildProcessWithoutNullStreams } from 'child_process';

export class ScriptManagerService {

    static readonly SCRIPT_DIRECTORY = homedir() + '/OSBot/Scripts';

    private startTime: number | null;
    private currentScript: ScriptStartRequest | null;

    private proc : ChildProcessWithoutNullStreams | null;
    private stdout: string[];
    private stderr: string[];

    private accounts: RunescapeAccount[];
    private osbotAccount: Account;

    constructor(accounts: RunescapeAccount[], osbotAccount: Account) {
        this.proc = null;
        this.startTime = null;
        this.currentScript = null;
        this.stdout = [];
        this.stderr = [];

        this.accounts = accounts;
        this.osbotAccount = osbotAccount;
    }

    public async getAllScripts() {
        let scripts = await fs.promises.readdir(ScriptManagerService.SCRIPT_DIRECTORY);
        return scripts.filter(script => script.endsWith('.jar')).map(script => script.replace(".jar", ""))
    }

    public async getAllAccounts() {
        return this.accounts;
    }

    public async start(startScriptRequest: ScriptStartRequest) : Promise<void | Error> {
        if (this.proc) {
            throw new Error(`A script is already, please terminate that script first!`);
        }

        const scripts = await this.getAllScripts();
        if(!scripts.includes(startScriptRequest.script_name)) {
            throw new Error(`The script named ${startScriptRequest.script_name} could not found.`);
        }

        const proc = spawn('java', [
                '-jar', 
                '../dependencies/OSBotApi.jar', 
                '-login', 
                `${this.osbotAccount.email}:${this.osbotAccount.password}`,
                '-bot',
                `${startScriptRequest.account.email}:${startScriptRequest.account.email}`,
                '-script',
                `${startScriptRequest.script_name}:${this.stringifyScriptParameters(startScriptRequest.script_parameters)}`
            ]
        );

        proc.stdout.on('data', data => {
            this.stdout.push(data);
        });

        proc.stderr.on('data', data => {
            this.stderr.push(data);
        });

        this.startTime = Date.now();
        this.currentScript = startScriptRequest;
    }

    public async end() {
        if(!this.proc) {
            return;
        }

        this.proc.kill();
        this.proc = null;
        this.stdout = [];
        this.stderr = [];
        this.startTime = null;
        this.currentScript = null;
    }

    public getStdOut(): string[] {
        return this.stdout;
    }

    public getStdErr(): string[] {
        return this.stderr;
    }

    public getCurrentScript() {
        if (this.currentScript) {
            return {
                script_name: this.currentScript?.script_name,
                start_time: this.startTime,
                account: this.currentScript?.account.email,
                input: this.currentScript?.script_parameters
            }
        }

        return {};
    }

    private stringifyScriptParameters(scriptParameters: Record<string, string>): string {
        let first = true;
        let params = "";

        for (const key of Object.keys(scriptParameters)) {
            let keyValuePair = `${key}:${scriptParameters[key]}`;

            if (first) {
                params += keyValuePair;
            } else {
                params += "," + keyValuePair;
            }

        }

        return params;
    }

}