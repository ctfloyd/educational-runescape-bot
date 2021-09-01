import screenshot from 'screenshot-desktop';
import fs, { PathLike } from 'fs';

// On linux this requires imagemagick
export class DesktopScreenshotService {
    
    private screenshotIntervalAmount : number;
    private screenshotInterval : NodeJS.Timer | null; 


    constructor(screenshotIntervalAmount: number) {
        this.screenshotInterval = null;
        this.screenshotIntervalAmount = screenshotIntervalAmount;
    }

    private async saveScreenshot() : Promise<void> {
        try {
            await screenshot({
                format: 'png',
                screen: 1,
                filename: 'screenshots/screenshot-' + Date.now() + '.png'
            });
        } catch (e) {
           console.log('Failed to take a screen shot at' + Date.now() + '.', e);
        }

        return;
    }    

    public start() : void {
        this.screenshotInterval = setInterval(() => this.saveScreenshot(), this.screenshotIntervalAmount);
    }

    public stop() : void {
        if (this.screenshotInterval) {
            clearInterval(this.screenshotInterval);
        }
    }

    public async getMostRecentScreenshotFilename() : Promise<PathLike> {
       const pngs = (await fs.promises.readdir('screenshots')).filter(file => file.endsWith('.png'));

       let latestUpdateTime: number = 0;
       let latestFile: PathLike = '';

       for (const png of pngs) {
           const path = 'screenshots/' + png;
           const stat = await fs.promises.stat(path);
           if (stat.birthtime.getTime() > latestUpdateTime) {
               latestUpdateTime = stat.birthtime.getTime();
               latestFile = path;
           }
       }

       return latestFile;
    }

}