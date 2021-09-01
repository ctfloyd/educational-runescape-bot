import { RunescapeAccount, Account, SecretsService } from '../interfaces/interfaces';


export class LocalSecretsService implements SecretsService {

    constructor() {}

    public getRunescapeAccounts(): RunescapeAccount[] {
        const email = process.env.RS_EMAIL;
        const password = process.env.RS_PASSWORD;
        const bankPin = process.env.RS_BANK_PIN;

        if(!email || !password) {
            throw new Error('The required environment variables for a runescape account are not defiend.');
        }

        return [
            {
                email: email,
                password: password,
                bankPin: bankPin ? Number(bankPin) : undefined
            }
        ];
        
    }

    public getOSBotAccount(): Account {
        const email = process.env.OSB_EMAIL;
        const password = process.env.OSB_PASSWORD;

        if(!email || !password) {
            throw new Error('The required environment variables for an osbot account are not defiend.');
        }

        return {
            email: email,
            password: password
        }
    }
}