import {inject, Injectable} from '@angular/core';
import {DataStore} from "../store/data.store";
import {DataService} from "./data.service";
import {IData} from "../store/store.definitions";

@Injectable({
  providedIn: 'root'
})
export class WrappingService {
  readonly datastore = inject(DataStore);
  readonly dataService = inject(DataService);

  addNew() {
    this.datastore.setLoading();
    const c = this.dataService.addElement({id:'', key:'', value:''}).subscribe({
      next: r => {
        this.datastore.addAll(r);
      },
      error: err => {
        console.log(err);
        this.datastore.setError('Failed to add record');
      },
      complete: () => {
        c.unsubscribe()
      }
    });
  }

  changeData(selected: IData) {
    if (selected) {
      const c = this.dataService.updateElement(selected).subscribe({
        next: r => {
          this.datastore.addAll(r);
        },
        error: err => {
          console.log(err);
          this.datastore.setError('Failed to add record');
        },
        complete: () => {
          c.unsubscribe()
        }
      });
    }
  }

  deleteRecord(selected: IData) {
    if (selected !== undefined) {
      this.datastore.setLoading();
      const c = this.dataService.removeElement(selected).subscribe({
        next: r => {
          this.datastore.addAll(r);
        },
        error: err => {
          console.log(err);
          this.datastore.setError('Failed to add record');
        },
        complete: () => {
          c.unsubscribe()
        }
      });
    }
  }
}
