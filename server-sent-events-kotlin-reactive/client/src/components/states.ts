import {FormEvent} from "react";


export class Message {
    public sender: string
    public message: string
    public newConnection: boolean

    constructor(sender: string, message: string, nc: boolean = false) {
        this.sender = sender;
        this.message=message;
        this.newConnection = nc;
    }
}

export interface AppState {
    user?: string,
    message?: string,
    registered?: false,
    users?: string[]
    messages?: Message[]
}

export interface MsgProps {
    fromMe: boolean
    sender: string,
    message: string,
    newConnection
}

export interface UserInputProps {
    value?: string | undefined,
    save: (value: String) => void
    ready: boolean
    register: () => void
}

export interface ChatViewProps {
    user?: string  | undefined
    value?: string | undefined,
    onMessage: (value: string) => void
    onSendMessage: () => void
    messages?: Message[] | undefined
    users?: string[] | undefined
}

export interface ConnectedUserProps {
    users?: string[] | undefined
}