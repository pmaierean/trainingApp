import { Component } from '@angular/core';
import { ContentComponent } from './components/content/content.component';
import {MatGridListModule} from '@angular/material/grid-list';

@Component({
  selector: 'app-root',
  imports: [ MatGridListModule, ContentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'SampleUI';
}
