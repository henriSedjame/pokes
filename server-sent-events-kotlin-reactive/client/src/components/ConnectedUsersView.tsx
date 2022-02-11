import React from "react";
import {ConnectedUserProps} from "./states";


export class ConnectedUsersView extends React.Component<ConnectedUserProps, any> {

    render() {
        return (
            <div className="col-2 min-vh-100 overflow-auto" style={{backgroundColor: "#3b3f41", height: "200px"}}>
                <div className="container-fluid pt-3 pb-2">
                    <p className="fw-bold text-white d-flex justify-content-center">
                        <div className="mx-2" style={{fontSize: "25px"}}> {this.props.users?.length} </div>
                        <div className="pt-2">ONLINE USERS</div>
                    </p>

                    <div className="bg-white px-1 mb-3" style={{height: 1}}> </div>
                    {
                        this.props.users?.map( u =>
                            <div>
                                <div className="d-xl-flex justify-content-xl-start align-items-xl-center mb-3 d-sm-block justify-content-sm-between align-items-sm-center">
                                    <div className=" bg-dark rounded-circle text-center" style={{width: "50px", height: "50px"}}>
                                        <p className="mt-1" style={{fontSize: "28px"}}>🧸</p>
                                    </div>

                                    <div className="px-2 text-white fw-bold"> {u.toUpperCase()} </div>
                                </div>
                                <div className="bg-dark mx-5 mb-3" style={{height: 0.5}}> </div>
                            </div>

                        )
                    }
                </div>

            </div>
        );
    }
}