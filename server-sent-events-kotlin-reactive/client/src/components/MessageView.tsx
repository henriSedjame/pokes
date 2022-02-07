import React from "react";
import {MsgProps} from "./states";



export class MessageView extends React.Component<MsgProps, any> {

    constructor(props) {
        super(props);
    }

    render() {
        let s;
        if (this.props.fromMe) {
            s="bg-light m-1 text-start";
        } else {
            s = "bg-success m-1 text-end";
        }

        return (
            <div  style={{height: 50}} className={s}>
                <strong>👤 {this.props.sender}</strong>
                <p className={ this.props.fromMe ? "text-black" : "text-white"}> {this.props.message}</p>
            </div>
        );
    }
}