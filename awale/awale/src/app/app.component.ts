import {Component, EventEmitter, NgZone, OnInit} from '@angular/core';
import {AwaleService} from "./awale.service";
import {GameChannel, Game, GameEvent, PlayerType} from "./shared";
import {EventService} from "./event.service";
import arrayShuffle from "array-shuffle";
import {RULES} from "./rules";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  game!: Game;

  pc_choice? : number;

  show_rules: boolean = false;

  rules = RULES;

  finished: boolean = false;

  winner?: Winner | null;

  constructor(private service: AwaleService, private eventService: EventService) {
    this.start();
  }

  public start() {
    this.finished = false;
    this.winner = null;
    this.show_rules = false;
    this.pc_choice = undefined;

    this.game = this.service.start()

    if (this.game.p2.turn) {
      this.pc_play();
    }
  }

  public choose(n: number, row1: boolean) {

    // @ts-ignore
    let b = n instanceof PointerEvent;

    if (!b) {
      n = Number(n);
      let i = row1 ? (6 - (n + 1)) : (n + 6);
      this.game = this.service.next(i, (this.game));

      let result = this.finishOrNot();

      if (!result.end) {

        if (this.game.p2.turn) {
          this.pc_play();
        }
      } else {
        this.eventService.endGameEmitter.next(true);
        this.finished =  true;
        this.winner = result.winner;
      }

    }
  }

  public showRules() {
    this.show_rules = !this.show_rules;
  }

  private finishOrNot(): Result {

    let check = (array: Uint8Array) => {
      return Array.from(array).some(n => n!= 0)
    }

    let userHoles = this.game.holes.slice(0, 6);

    let pcHoles = this.game.holes.slice( 6, 12);

    let userEmpty = !check(userHoles);
    let pcEmpty = !check(pcHoles);

    let winner = null;

    let finished = userEmpty || pcEmpty;

    if (finished) {

      if (userEmpty){
        userHoles.forEach(
          pt =>  {
            this.game.p2.points += pt;
          }
        )
      } else  {
        pcHoles.forEach(
          pt =>  {
            this.game.p1.points += pt;
          }
        )
      }

      let userPoints = this.game.p1.points;
      let pcPoints = this.game.p2.points;

      winner = (userPoints == pcPoints)
        ? Winner.EQUALITY
        : ((userPoints > pcPoints) ? Winner.USER : Winner.PC)

    }

    return new Result(finished, winner);
  }

  private pc_play() {
    setTimeout(() => {
      let indexes = this.service.pc_choice(this.game.holes);
      let numbers = arrayShuffle(Array.from(indexes));
      this.eventService.pcChoiceEmitter.next(numbers[0]);

    }, 500)
  }

}

class Result {
  constructor(
    public end: boolean,
    public winner?: Winner | null
  ) {
  }
}

export enum Winner {
  USER = 'USER',
  PC = 'PC',
  EQUALITY = 'EQUALITY'
}
