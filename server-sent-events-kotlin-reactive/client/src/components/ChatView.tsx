import React, {FormEvent} from "react";
import {MessageView} from "./MessageView";
import {ChatViewProps} from "./states";

export class ChatView extends React.Component<ChatViewProps, any> {

    messageInput?: HTMLInputElement | null

    onWritingMessage = (evt: FormEvent<HTMLInputElement>, fn: (string) => void) => {
        evt.preventDefault();
        fn(this.messageInput?.value)
    }

    render() {
        return (
            <div className="container-fluid mt-5" style={{maxWidth: '600px'}}>

                <div className="input-group mb-3">
                    <div className="input-group-prepend mx-auto">
                        <span className="input-group-text text-center" id="basic-addon2" style={{height: 60, width: 60}}>✍️</span>
                    </div>
                    <input type="text"
                           className="form-control"
                           placeholder="Your message"
                           aria-label="Message"
                           aria-describedby="basic-addon2"
                           id="message"
                           ref={i => this.messageInput = i}
                           onInput={(e) => this.onWritingMessage(e, this.props.onMessage)}
                           value={this.props.value}
                           required/>
                    <button className={ (this.props.value !== '') ? "btn btn-primary btn-lg": "btn btn-light btn-lg"}
                            onClick={(this.props.value !== '') ? this.props.onSendMessage : null}>
                        SEND
                    </button>
                </div>
                <div style={{height: 600}} className="bg-dark overflow-auto">
                    {
                        this.props.messages?.map(msg =>
                            <MessageView
                                key={this.props.messages?.indexOf(msg)}
                                fromMe={msg.sender == this.props.user}
                                sender={msg.sender}
                                message={msg.message}/>)
                    }

                </div>
                <div className="bg-info p-1">
                    <p className="text-success text-center fw-bold text-white"> CONNECTED USERS </p>
                    <div className="d-flex">
                        {
                            this.props.users?.map(u => <div className="bg-dark rounded m-2 text-white p-1"> 👤 {u} </div>)
                        }
                    </div>
                </div>
            </div>
        );
    }
}