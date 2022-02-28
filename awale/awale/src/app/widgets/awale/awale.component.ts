import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game} from "../../shared";
import {HoleClickEvent} from "../../models/models";

@Component({
  selector: 'app-awale',
  templateUrl: './awale.component.html',
  styleUrls: ['./awale.component.css']
})
export class AwaleComponent implements OnInit {


  @Input()
  game!: Game

  @Input()
  user_loading_value!: number;

  @Input()
  pc_loading_value!: number;

  @Input()
  user_count!: number;

  @Input()
  pc_count!: number;

  @Output()
  onHoleClick: EventEmitter<HoleClickEvent> = new EventEmitter<HoleClickEvent>();

  @Input()
  changeCode!: string;

  @Input()
  gain!: boolean;

  constructor() { }

  ngOnInit(): void {
  }

  click(nb: number, b: boolean) {
    this.onHoleClick.emit(new HoleClickEvent(nb, b));
  }
}
