
import React, {EventHandler, FormEvent, KeyboardEvent, KeyboardEventHandler} from "react";
import {UserInputProps} from "../models/Props";

export class EntryView extends React.Component<UserInputProps, any> {

    usernameInput?: HTMLInputElement |  null

    onWritingUsername = (evt: FormEvent<HTMLInputElement>, fn: (val: string) => void) => {
        evt.preventDefault();
        fn(this.usernameInput?.value!)
    }

    onEnterPressed = (evt: KeyboardEvent<HTMLInputElement>) => {
        if (evt.key === 'Enter' && this.props.ready) {
            this.props.register()
        }
    }

    render() {
        return (
            <div className="col-10 min-vh-100 bg-dark d-flex align-items-center justify-content-center">
                <div className=" align-items-center col-xl-6 col-md-8 col-sm-10 ">
                    <div className="input-group  rounded-pill col-12 d-flex align-items-center justify-content-center px-2"  style={{height: 60, backgroundColor: "#3B3F41FF"}}>
                        <input type="text"
                               className="form-control rounded-pill bg-dark text-white"
                               style={{height: 50}}
                               placeholder="Enter your login and click ENTER button to start chatting"
                               aria-label="Username"
                               id="user"
                               ref={i => this.usernameInput = i}
                               onInput={(e) => this.onWritingUsername(e, this.props.save)}
                               value={this.props.value || ''}
                               onKeyPress={this.onEnterPressed}
                               required/>
                        <div className="mx-1">
                            <button className={ this.props.ready ? "btn btn-dark btn-lg shadow rounded-pill ": "btn btn-light btn-lg rounded-pill"} onClick={this.props.ready ? this.props.register : undefined}> ENTER </button>
                        </div>
                    </div>
                </div>

            </div>

        );
    }


}