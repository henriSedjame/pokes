import {retry} from "rxjs";

export enum PlayerType {
  USER,
  PC
}

export class Player {
  public points: number = 0;
  public turn: boolean = false;
  public readonly pType: PlayerType;

  constructor(type: PlayerType) {
    this.pType = type;
  }

  get get_points() {
    return this.points;
  }

  set set_points(points: number) {
    this.points = points;
  }

  get get_turn() {
    return this.turn
  }

  set set_turn(b: boolean) {
    this.turn = b;
  }
}


export class GameEvent {
  constructor(
    public running: boolean,
    public index: number,
    public nb: number,
    public count: number,
    public player?: boolean,
    public gain?: number,

  ) {}
}

export class Game {

  constructor(
    public p1: Player,
    public p2: Player,
    public holes: Uint8Array) {
  }


  get get_holes() {
    return this.holes;
  }

  get get_p1() {
    return this.p1;
  }

  get get_p2() {
    return this.p2;
  }

}

export class GameChannel {
  constructor(public channel: MessageChannel) {
  }

  public send(evt: GameEvent) {
    this.channel.port1.postMessage(evt);
  }
}




