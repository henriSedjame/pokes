import React from "react";
import {MsgProps} from "./states";



export class MessageView extends React.Component<MsgProps, any> {

    constructor(props) {
        super(props);
    }

    render() {
        let content;


        if (this.props.newConnection && !this.props.fromMe) {

            content =  <div className="  mx-2 my-1 col-12 d-flex p-2" style={{backgroundColor: "#3B3F41FF", color: "grey"}}>
                🔔 {this.props.sender} vient de se connecter
            </div>
        } else {
            content =  <div className="  mx-2 my-1 d-flex col-12 p-2" style={{backgroundColor: "#3B3F41FF"}}>
                <div className=" bg-dark rounded-circle text-center" style={{width: "50px", height: "50px"}}>
                    <div className="mt-1" style={{fontSize: "28px"}}>🧸</div>
                </div>

                <div>
                    <div className="px-2 text-white fw-bold"> {this.props.sender.toUpperCase()} </div>
                    <div className="px-3 text-white fw-light"> {this.props.message}</div>
                </div>
                <br/>
            </div>
        }
        return content;
    }
}