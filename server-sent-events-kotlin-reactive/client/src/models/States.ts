import {Message} from "./Data";

export interface AppState {
    user?: string,
    message?: string,
    registered: boolean,
    users: string[]
    messages: Message[]
}