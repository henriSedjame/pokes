
export class Message {
    public sender: string
    public message: string
    public newConnection: boolean
    public moderatorMessage: boolean

    constructor(sender: string, message: string, nc: boolean = false, moderator: boolean = false) {
        this.sender = sender;
        this.message=message;
        this.newConnection = nc;
        this.moderatorMessage = moderator;
    }
}
