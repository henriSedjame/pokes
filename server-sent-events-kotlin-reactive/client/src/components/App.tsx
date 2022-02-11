import React from 'react'

import './App.css'
import {AppState, Message} from "./states";

import axios from 'axios'
import {EventType} from "./events";
import {EntryView} from "./EntryView";
import {ChatView} from "./ChatView";
import {ConnectedUsersView} from "./ConnectedUsersView";

const http = axios.create({
    baseURL: "http://localhost:8080/chat",
    headers: {
        "Content-type": "application/json"
    }
});

const eventSource = new EventSource("http://localhost:8080/chat/events");

export class App extends React.Component<any, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
            user: undefined,
            registered: false,
            messages: [],
            users: []
        }

    }

    componentDidMount() {
        eventSource.onmessage = (evt) => {
            let data = JSON.parse(evt.data);

            switch (data.type) {
                case EventType.NEW_PARTICIPANT:
                    if (data.name !== this.state.user ) {
                        if (this.state.registered) {
                            this.setState({messages: [new Message(data.name, "", true), ...this.state.messages]})
                        }
                    } else {
                        this.setState({registered: true})
                    }
                    this.setState({users: [data.name, ...this.state.users]})
                    break
                case EventType.NEW_MESSAGE:
                    this.setState({messages: [new Message(data.author, data.message), ...this.state.messages]})
                    if (data.author === this.state.user) {
                        this.setState({message: ''})
                    }
                    break
                case EventType.MODERATOR_MESSAGE: {
                    if (data.author === this.state.user) {
                        this.setState({messages: [new Message(data.author, data.message, false, true), ...this.state.messages]})
                    }
                    break
                }
                case EventType.ERROR: {
                    if (data.receiver == this.state.user) {
                        alert(data.message)
                    }
                }
            }
        }
    }

    register = () => {
        http.get("/" + this.state.user)
            .catch(e => alert(e.message))
    }

    sendMessage = () => {
        this.setState({message: ''})
        http.post("/" + this.state.user, {message: this.state.message})
            .catch(e => alert(e.message))
    }

    render() {

        return (
            <div className="container min-vw-100 min-vh-100 bg-dark" style={{backgroundColor: "#2b2f36"}}>
                <div className="row row-cols-2">
                    <ConnectedUsersView users={this.state.users}/>

                    {
                        this.state.registered
                            ? <ChatView
                                user={this.state.user}
                                value={this.state.message}
                                messages={this.state.messages}
                                users={this.state.users}
                                onMessage={(msg) => this.setState({message: msg})}
                                onSendMessage={this.sendMessage}/>
                            : <EntryView
                                value={this.state.user}
                                save={(name) => this.setState({user: name})}
                                ready={this.state.user !== undefined && this.state.user != ''}
                                register={this.register}/>
                    }

                </div>


            </div>
        );
    }

}
