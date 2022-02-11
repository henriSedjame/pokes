import React, {FormEvent} from "react";
import {MessageView} from "./MessageView";
import {ChatViewProps} from "./states";

export class ChatView extends React.Component<ChatViewProps, any> {

    messageInput?: HTMLInputElement | null

    onWritingMessage = (evt: FormEvent<HTMLInputElement>, fn: (msg: string) => void) => {
        evt.preventDefault();
        let value = this.messageInput?.value || '';
        fn(value)
    }



    render() {


        return (
            <div className="col-10 min-vh-100 bg-dark ">

               <div className="overflow-auto align-items-end justify-content-start d-flex" style={{height: "90%"}}>
                   <div className="container ">
                       <div className="row">
                           {
                               this.props.messages?.reverse().map(msg =>
                                   <MessageView
                                       key={this.props.messages?.indexOf(msg)}
                                       fromMe={msg.sender == this.props.user}
                                       sender={msg.sender}
                                       message={msg.message}
                                       newConnection={msg.newConnection}
                                   />)
                           }
                       </div>

                   </div>

               </div>
                <div className=" d-flex align-items-center justify-content-center px-5"  style={{height: "10%"}}>

                        <div className=" align-items-center col-xl-6 col-md-8 col-sm-10 ">
                            <div className="input-group  rounded-pill col-12 d-flex align-items-center justify-content-center px-2"  style={{height: 60, backgroundColor: "#3B3F41FF"}}>


                            <input type="text"
                                   className="form-control rounded-pill bg-dark text-white"
                                   placeholder="Your message"
                                   aria-label="Message"
                                   style={{ height: 50}}
                                   aria-describedby="basic-addon2"
                                   id="message"
                                   ref={i => this.messageInput = i}
                                   onInput={(e) => this.onWritingMessage(e, this.props.onMessage)}
                                   value={this.props.value}
                                   required/>

                            <div className="mx-1 " style={{top: 6, right: 8}}>
                                <button
                                    className={(this.props.value !== '') ? "btn btn-dark btn-lg shadow rounded-pill" : "btn btn-light btn-lg shadow rounded-pill"}
                                    onClick={(this.props.value !== '') ? this.props.onSendMessage : undefined}>
                                    SEND
                                </button>

                            </div>

                        </div>
                    </div>

                </div>
            </div>
        );
    }
}