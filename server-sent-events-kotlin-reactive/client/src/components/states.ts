import {FormEvent} from "react";


export class Message {
    public sender: string
    public message: string

    constructor(sender: string, message: string) {
        this.sender = sender;
        this.message=message;
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
    message: string
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