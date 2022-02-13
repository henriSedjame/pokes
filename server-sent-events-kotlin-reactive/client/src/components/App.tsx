import React from 'react'

import './App.css'
import {AppState} from "../models/States";
import {EntryView} from "./EntryView";
import {ChatView} from "./ChatView";
import {ConnectedUsersView} from "./ConnectedUsersView";
import {Message} from "../models/Data";
import {ChatClient} from "../services/ChatClient";

export class App extends React.Component<any, AppState> {

    private client!: ChatClient;

    constructor(props: any) {
        super(props);
        this.state = {
            registered: false,
            messages: [],
            users: []
        }

    }

    componentDidMount() {

        this.client = new ChatClient(
            (data) => {
                if (data.name !== this.state.user ) {
                    if (this.state.registered) {
                        this.setState({messages: [new Message(data.name, "", true), ...(this.state.messages)]})
                    }
                } else {
                    this.setState({registered: true})
                }
                this.setState({users: [data.name, ...(this.state.users)]})
            },
            (data) => {
                this.setState({messages: [new Message(data.author, data.message), ...(this.state.messages)]})
                if (data.author === this.state.user) {
                    this.setState({message: ''})
                }
            },
            (data) => {
                if (data.author === this.state.user) {
                    this.setState({messages: [new Message(data.author, data.message, false, true), ...(this.state.messages)]})
                }
            },
            (data) => {
                if (data.receiver == this.state.user) {
                    alert(data.message)
                }
            }
        )

    }

    componentWillUnmount() {
        this.client.close()
    }

    register = () =>
        this.client.register(this.state.user!);

    sendMessage = () =>
        this.client.sendMessage(this.state.user!, this.state.message!, () => this.setState({message: ''}));


    render() {
        return (
            <div className="container min-vw-100 min-vh-100 bg-dark" style={{backgroundColor: "#2b2f36"}}>
                <div className="row row-cols-2">
                    <ConnectedUsersView users={this.state.users!}/>

                    {
                        this.state.registered
                            ? <ChatView
                                user={this.state.user!}
                                value={this.state.message!}
                                messages={this.state.messages}
                                users={this.state.users}
                                onMessage={(msg: string) => this.setState({message: msg})}
                                onSendMessage={this.sendMessage}/>
                            : <EntryView
                                value={this.state.user!}
                                save={(name: string) => this.setState({user: name })}
                                ready={this.state.user !== undefined && this.state.user != ''}
                                register={this.register}/>
                    }

                </div>


            </div>
        );
    }

}
