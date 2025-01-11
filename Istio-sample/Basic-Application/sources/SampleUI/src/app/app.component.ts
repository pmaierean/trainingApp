import { Component } from '@angular/core';
import { ContentComponent } from './components/content/content.component';
import {MatGridListModule} from '@angular/material/grid-list';
import {ToolbarComponent} from "./components/toolbar/toolbar.component";
import {DataStore} from "./store/data.store";
import {DataService} from "./services/data.service";
import {WrappingService} from "./services/wrapping.service";

@Component({
  selector: 'app-root',
  imports: [ MatGridListModule, ContentComponent, ToolbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [DataStore, DataService, WrappingService]
})
export class AppComponent {
  title = 'SampleUI';
}
