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

export class HoleState {
  constructor(
    public index: number,
    public nb: number) {
  }
}

export class GameEvent {
  constructor(
    public states: HoleState[],
    public gain: number,
    public end: boolean
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





