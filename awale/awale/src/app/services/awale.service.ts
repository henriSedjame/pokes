import { Injectable } from '@angular/core';
import * as wasm from 'awale-lib';
import {Game, GameEvent} from "../shared";
import {EventService} from "./event.service";


@Injectable({
  providedIn: 'root'
})
export class AwaleService {

  constructor(private eventService: EventService) {
  }

  public start(): Game {
    let number = Math.random();
    let g = wasm.start(number > 0.5);
    this.eventService.updateGameEmitter.next(true);
    return g;
  }

  public pc_choice(state: Uint8Array): Uint32Array {
    return wasm.pc_choice(state);
  }

  public next_turn(i: number, game: Game): GameEvent {
    let evt = wasm.next_turn(i, game.holes);
    this.eventService.updateGameEmitter.next(true);
    return evt;
  }
}
