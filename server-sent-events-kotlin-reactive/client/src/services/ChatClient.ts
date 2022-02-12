import {ErrorEvent, EventType, NewMessageEvent, NewUserEvent} from "../models/Events";

type Consumer<T> = (t: T) => void

export class ChatClient {

    constructor(
        private onNewUser: Consumer<NewUserEvent>,
        private onNewMessage: Consumer<NewMessageEvent>,
        private onNewModeratorMessage: Consumer<NewMessageEvent>,
        private onError: Consumer<ErrorEvent>
    ) {
        new EventSource("/chat/events").onmessage = (evt) => {
            let data = JSON.parse(evt.data);
            switch (data.type) {
                case EventType.NEW_PARTICIPANT:
                    this.onNewUser(data)
                    break
                case EventType.NEW_MESSAGE:
                    this.onNewMessage(data)
                    break
                case EventType.MODERATOR_MESSAGE: {
                    this.onNewModeratorMessage(data)
                    break
                }
                case EventType.ERROR: {
                    this.onError(data)
                }
            }
        }
    }

    public register(name: string){
        fetch(`/chat/${name}`)
            .catch(e => alert(e.message))
    }

    public sendMessage(name: string, message: string, fn: () => void){
        fn()
        fetch(`/chat/${name}`, {
            method: 'POST',
            body: JSON.stringify({message: message}),
            headers: {
                "Content-Type" : "application/json"
            }
        }).catch(e => alert(e.message))
    }

}