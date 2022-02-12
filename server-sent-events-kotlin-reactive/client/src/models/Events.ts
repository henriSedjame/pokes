
export enum EventType {
    NEW_PARTICIPANT = "NEW_PARTICIPANT",
    NEW_MESSAGE = "NEW_MESSAGE",
    MODERATOR_MESSAGE = "MODERATOR_MESSAGE",
    ERROR = "ERROR"
}

export interface NewUserEvent {
    name: string
}

export interface NewMessageEvent {
    author: string,
    message: string,
}

export interface  ErrorEvent {
    receiver: string,
    message: string
}