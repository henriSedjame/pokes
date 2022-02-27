import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {EventService} from "../../services/event.service";


@Component({
  selector: 'app-hole',
  templateUrl: './hole.component.html',
  styleUrls: ['./hole.component.css']
})
export class HoleComponent implements OnInit {

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

  @Output()
  click: EventEmitter<number> = new EventEmitter<number>()

  constructor(private eventService: EventService) { }

  ngOnInit(): void {
    this.eventService.pcChoiceEmitter.subscribe({
     next: i => {
       if ( !this.userhole && (i - 6) == this.index ) {
         this.press();
       }
     }
    });

    this.eventService.endGameEmitter.subscribe({
      next: end => {
        this.endGame = end;
      }
    })

  }

  press() {
    this.selected = true;
    setTimeout(() =>{
      this.selected = false;
      this.click.emit(this.index);
    }, 500)
  }


}
