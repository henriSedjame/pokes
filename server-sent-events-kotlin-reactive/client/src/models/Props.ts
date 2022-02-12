import {Message} from "./Data";

export interface MsgProps {
    fromMe: boolean
    sender: string,
    message: string,
    newConnection: boolean,
    moderator: boolean
}

export interface UserInputProps {
    value?: string
    save: (value: string) => void
    ready: boolean
    register: () => void
}

export interface ChatViewProps {
    user: string
    value?: string
    onMessage: (value: string) => void
    onSendMessage: () => void
    messages: Message[]
    users: string[]
}

export interface ConnectedUserProps {
    users: string[]
}