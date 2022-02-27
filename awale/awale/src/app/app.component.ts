import {Component} from '@angular/core';
import {AwaleService} from "./services/awale.service";
import {Game} from "./shared";
import {EventService} from "./services/event.service";
import arrayShuffle from "array-shuffle";
import {RULES} from "./models/rules";
import {HoleClickEvent, Result, Winner} from "./models/models";
import {Levels} from "./models/levels";

const INTERVAL = 100;

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

  started: boolean = false;

  finished: boolean = false;

  winner: Winner | null = null;

  user_loading: number = 0;

  pc_loading: number = 0;

  interval!: number;

  levels = [
    Levels.LEVEL_1,
    Levels.LEVEL_2,
    Levels.LEVEL_3
  ]

  game_level = Levels.LEVEL_1;

  constructor(private service: AwaleService, private eventService: EventService) {

    this.eventService.updateGameEmitter.subscribe({
        next: (updated) => {
          if (updated) {
            clearInterval(this.interval);
            this.pc_loading = 0;
            this.user_loading = 0;
            this.chrono();
          }
        }
      })
  }

  public start() {
    this.finished = false;

    this.winner = null;
    this.show_rules = false;
    this.pc_choice = undefined;

    setTimeout(()=> {
      this.started = true;
      this.game = this.service.start()

      if (this.game.p2.turn) {
        this.pc_play();
      }
    }, 500)

  }

  public abandon() {
    clearInterval(this.interval);
    this.eventService.endGameEmitter.next(true);
    this.finished = true;
    this.started = false;
    this.winner = Winner.PC;
  }

  public choose(evt: HoleClickEvent) {

    let n = evt.value;
    let row1  = evt.user;

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
        clearInterval(this.interval);
        this.eventService.endGameEmitter.next(true);
        this.finished =  true;
        this.winner = result.winner;
      }

    }
  }

  public showRules() {
    this.show_rules = !this.show_rules;
  }

  public chrono() {
    this.interval = setInterval(() => {
      if (this.game.p1.turn) {
        this.user_loading += (INTERVAL/100);
        if (this.user_loading >= 100) {
          clearInterval(this.interval);
          this.game.p1.turn = false;
          this.user_loading=0;
          this.game.p2.turn = true;
          this.pc_play();
        }
      } else {
        this.pc_loading += (this.game_level/100);
        if (this.pc_loading >= 100) {
          clearInterval(this.interval);
          this.game.p1.turn = true;
          this.game.p2.turn = false;
          this.pc_loading=0;
        }
      }
    }, this.game_level);
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

  public level_Label(l: Levels) {
    switch (l) {
      case Levels.LEVEL_1:
        return "NIVEAU 1 (Débutant)";
      case Levels.LEVEL_2:
        return "NIVEAU 2 (Intermédiaire)";
      case Levels.LEVEL_3:
        return "NIVEAU 3 (Expert)"
    }
  }

  changeLevel(evt: any) {
    console.log(evt)
  }
}


