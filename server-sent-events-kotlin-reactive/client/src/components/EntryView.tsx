import React, {FormEvent} from "react";
import {UserInputProps} from "./states";

export class EntryView extends React.Component<UserInputProps, any> {

    usernameInput?: HTMLInputElement | null

    onWritingUsername = (evt: FormEvent<HTMLInputElement>, fn: (string) => void) => {
        evt.preventDefault();
        fn(this.usernameInput?.value)
        console.log(this.state.user)
    }

    render() {
        return (
            <div className="container-fluid mt-5" style={{maxWidth: '600px'}}>
                <div className="input-group mb-3">
                    <div className="input-group-prepend mx-auto">
                        <span className="input-group-text text-center" id="basic-addon1" style={{height: 60, width: 60}}>👤</span>
                    </div>
                    <input type="text"
                           className="form-control"
                           placeholder="Enter your username"
                           aria-label="Username"
                           aria-describedby="basic-addon1"
                           id="user"
                           ref={i => this.usernameInput = i}
                           onInput={(e) => this.onWritingUsername(e, this.props.save)}
                           value={this.props.value}
                           required/>
                </div>
                <div className="mb-3">
                    <button className={ this.props.ready ? "btn btn-primary btn-lg": "btn btn-light btn-lg"} onClick={this.props.ready ? this.props.register : null}> ENTER </button>
                </div>
            </div>
        );
    }
}