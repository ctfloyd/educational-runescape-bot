export interface ScriptRoutes {
    all: string
    accounts: string,
    start: string,
    end: string 
    current: string,
    stdout: string,
    stderr: string
}

export interface Account {
    email: string
    password: string
}

export interface RunescapeAccount extends Account {
    bankPin?: number
}

export interface ApiRunescapeAccount {
    email: string
}

export interface ScriptStartRequest {
    script_name: string,
    account: Account,
    script_parameters: Record<string, string>
}

export interface ApiScriptStartRequest {
    script_name: string,
    account: ApiRunescapeAccount,
    script_parameters: Record<string, string>
}

export interface SecretsService {
    getRunescapeAccounts(): RunescapeAccount[],
    getOSBotAccount(): Account
}

export const isApiScriptStartRequest = (o: unknown): o is ApiScriptStartRequest => {
    if (typeof o !== 'object') {
        return false;
    }

    const candidate = o as ScriptStartRequest;

    if (candidate.script_name === 'undefined') {
        return false;
    }

    if (typeof candidate.account !== 'object' || typeof candidate.account.email !== 'string') {
        return false;
    }

    if (typeof candidate.script_parameters !== 'object') {
        return false;
    }

    return false;
}