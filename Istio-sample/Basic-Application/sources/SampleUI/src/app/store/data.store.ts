import {patchState, signalStore, withHooks, withMethods, withState} from '@ngrx/signals';
import {IData} from './store.definitions';
import {DataService} from '../services/data.service';
import {inject} from '@angular/core';

type DataState = {
  data: IData[];
  isLoading: boolean;
  error: string;
}

const initialState: DataState = {
  data: [],
  isLoading: false,
  error: "",
}

export const DataStore = signalStore(
  withState<DataState>(initialState),
  withMethods((store) => ({
    getData: () => store.data,
  })),
  withHooks({
    onInit(store) {
      const dataService = inject(DataService);
      const c = dataService.getAll().subscribe({
        next: r => {
          if (r.code === 0) {
            patchState(store, {data: r.data.data, isLoading: false, error: ''});
          }
          else {
            patchState(store, {data: [], isLoading: false, error: r.message});
          }
        },
        error: err => {
          patchState(store, {data: [], isLoading: false, error: 'Failed to load data.'});
        },
        complete: () => {
          c.unsubscribe()
        }
      })
    },
    onDestroy(store) {

    }
  })
)
