import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-score',
  templateUrl: './score.component.html',
  styleUrls: ['./score.component.css']
})
export class ScoreComponent implements OnInit {

  @Input()
  user_score!: number

  @Input()
  pc_score!: number

  @Input()
  show_rules!: boolean

  constructor() { }

  ngOnInit(): void {
  }

}
