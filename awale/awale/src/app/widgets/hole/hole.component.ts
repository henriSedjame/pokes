import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {EventService} from "../../services/event.service";
import {Subscription} from "rxjs";


@Component({
  selector: 'app-hole',
  templateUrl: './hole.component.html',
  styleUrls: ['./hole.component.css']
})
export class HoleComponent implements OnInit,OnDestroy {

  @Input()
  active!: boolean

  @Input()
  index!:number

  @Input()
  value!: number

  @Input()
  userhole!: boolean

  selected: boolean = false;

  endGame: boolean = false;

  @Input()
  changing!: boolean;

  @Input()
  gain!: boolean;

  subscriptions: Subscription[] = [];

  @Output()
  click: EventEmitter<number> = new EventEmitter<number>()

  constructor(private eventService: EventService) { }

  ngOnInit(): void {

    let s1 = this.eventService.pcChoiceEmitter.subscribe({
     next: i => {
       if (!this.userhole && (i - 6) == this.index ) {
         this.press();
       }
     }
    });

    let s2 = this.eventService.endGameEmitter.subscribe({
      next: end => {
        this.endGame = end;
      }
    });

    this.subscriptions.push(s1, s2)
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((s) => s.unsubscribe());
  }

  press() {
    this.selected = true;
    setTimeout(() =>{
      this.selected = false;
      this.click.emit(this.index);
    }, 500)
  }




}
