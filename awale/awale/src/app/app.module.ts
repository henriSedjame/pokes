import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HoleComponent } from './widgets/hole/hole.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import { ScoreComponent } from './widgets/score/score.component';
import { GameResultComponent } from './widgets/game-result/game-result.component';
import { AwaleRowComponent } from './widgets/awale-row/awale-row.component';
import { AwaleComponent } from './widgets/awale/awale.component';
import { LogoComponent } from './widgets/logo/logo.component';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatRadioModule} from "@angular/material/radio";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    HoleComponent,
    ScoreComponent,
    GameResultComponent,
    AwaleRowComponent,
    AwaleComponent,
    LogoComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatProgressBarModule,
    MatRadioModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
