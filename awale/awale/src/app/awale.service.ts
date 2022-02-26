import { Injectable } from '@angular/core';
import * as wasm from 'awale-lib';
import {Game} from "./shared";


@Injectable({
  providedIn: 'root'
})
export class AwaleService {

  public start(): Game {
    let number = Math.random();
    return wasm.start(number > 0.5);
  }

  public next(i: number, game: Game): Game {
     return wasm.run(game, i);
  }

  public pc_choice(state: Uint8Array): Uint32Array {
    return wasm.pc_choice(state);
  }
}
