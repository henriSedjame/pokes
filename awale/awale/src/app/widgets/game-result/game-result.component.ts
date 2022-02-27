import {Component, Input, OnInit} from '@angular/core';
import {Winner} from "../../models/models";


@Component({
  selector: 'app-game-result',
  templateUrl: './game-result.component.html',
  styleUrls: ['./game-result.component.css']
})
export class GameResultComponent implements OnInit {


  @Input()
  winner!: Winner | null

  @Input()
  show!: boolean

  constructor() { }

  ngOnInit(): void {
  }

}
