import express from 'express';
import dotenv from 'dotenv';
import { SecretsService } from './interfaces/interfaces';
import { ScriptResource } from './resource/ScriptResource';
import { LocalSecretsService } from './service/LocalSecretsService';
import { RemoteSecretsResource } from './service/RemoteSecretsService';
import { ScriptManagerService } from './service/ScriptManagerService';

dotenv.config();
const app = express();

let secretsService: SecretsService;

if (process.env.IN_AWS) {
    secretsService = new RemoteSecretsResource();
} else {
    secretsService = new LocalSecretsService();
}

const scriptManagerService = new ScriptManagerService(secretsService.getRunescapeAccounts(), secretsService.getOSBotAccount());

const scriptResource = new ScriptResource(scriptManagerService);
scriptResource.initialize();

const scriptRouter = scriptResource.getRouter();
if(scriptRouter) {
    app.use(ScriptResource.PREFERRED_MOUNTING_LOCATION, scriptRouter);
}

app.listen(80, () => {
    console.log('EducationalBotters Script Manager starting on port 80.');
});