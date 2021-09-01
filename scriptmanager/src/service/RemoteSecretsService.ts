import { Account, RunescapeAccount, SecretsService } from "../interfaces/interfaces";

export class RemoteSecretsResource implements SecretsService {

    getRunescapeAccounts(): RunescapeAccount[] {
        throw new Error("Method not implemented.");
    }
    getOSBotAccount(): Account {
        throw new Error("Method not implemented.");
    }

} 