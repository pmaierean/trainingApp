import {patchState, signalStore, withComputed, withHooks, withMethods, withState} from '@ngrx/signals';
import {IData, IDataCollectionResponse} from './store.definitions';
import {DataService} from '../services/data.service';
import {computed, inject} from '@angular/core';

type DataState = {
  data: IData[];
  selected: string;
  hasChanges: boolean;
  isLoading: boolean;
  error: string;
}

const initialState: DataState = {
  data: [],
  selected: '',
  hasChanges: false,
  isLoading: false,
  error: "",
}

export const DataStore = signalStore(
  withState(initialState),
  withComputed(({data, selected, error}) => ({
    hasSelected: computed(() => selected() !== undefined),
    getDataSize: computed(() => data().length),
    hasError: computed(() => error() !== ""),
    getCurrentSelected: computed(() => {
      const id = selected()
      let ret: IData | undefined = undefined;
      data().forEach((record) => {
        if (record.id === id) {
          ret = record;
        }
      });
      return ret;
    }),
  })),
  withMethods((store) => ({
    addAll: (r: IDataCollectionResponse) => {
      if (r.code === 0) {
        patchState(store, {data: r.data.data, selected: r.selected, isLoading: false, error: ''});
      }
      else {
        patchState(store, {data: [], selected: '', isLoading: false, error: r.message});
      }
    },
    setError: (error: string) => patchState(store, {selected: '', isLoading: false, error: error}),
    setSelected: (selected: string) => patchState(store, {selected: selected}),
    setLoading: () => patchState(store, {isLoading: true})
  })),
  withHooks({
    onInit(store) {
      const dataService = inject(DataService);
      store.setLoading();
      const c = dataService.getAll().subscribe({
        next: r => {
          store.addAll(r);
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
