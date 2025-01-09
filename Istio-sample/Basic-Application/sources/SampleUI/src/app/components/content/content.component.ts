import {Component, inject, OnDestroy} from '@angular/core';
import {DataStore} from '../../store/data.store';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {DataService} from '../../services/data.service';
import {Subscription} from 'rxjs';
import {IData} from '../../store/store.definitions';
import {toObservable} from '@angular/core/rxjs-interop';

interface IElement {
  key: string;
  value: string;
}

@Component({
  selector: 'app-content',
  imports: [MatTableModule],
  templateUrl: './content.component.html',
  styleUrl: './content.component.css',
  providers: [DataStore]
})
export class ContentComponent implements OnDestroy {
  readonly dataStore = inject(DataStore);
  c?: Subscription;
  data?: IData[];
  displayedColumns = ['key', 'value'];
  dataSource?:IElement[];
  constructor(private readonly dataService: DataService) {
    this.c = toObservable(this.dataStore.getData()).subscribe(value => {
      console.log('value', value);
      this.data = value;
      let position = 0;
      const d = new Array<IElement>();
      this.data?.forEach(v => {
        d.push({
          key: v.key,
          value: v.value
        })
      });
      this.dataSource = d;
    });
  }

  ngOnDestroy(): void {
    if (this.c) {
      this.c.unsubscribe()
    }
  }

}
