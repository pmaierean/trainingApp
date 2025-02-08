import {Component, inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DataStore} from '../../store/data.store';
import {Subscription} from 'rxjs';
import {IData} from '../../store/store.definitions';
import {toObservable} from '@angular/core/rxjs-interop';
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatInputModule} from "@angular/material/input";
import {FormGroup, FormControl, Validators, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {WrappingService} from "../../services/wrapping.service";

interface IElement {
  id: string;
  key: string;
  value: string;
  selected: boolean;
}

@Component({
  selector: 'app-content',
  imports: [MatTableModule, MatPaginator, MatInputModule, FormsModule, ReactiveFormsModule],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})
export class ContentComponent implements OnDestroy {
  readonly numberPerPage = 5;
  readonly dataStore = inject(DataStore);
  readonly wrappingService= inject(WrappingService);
  subscription1?: Subscription;
  subscription2?: Subscription;
  subscription3?: Subscription;
  displayedColumns = ['key', 'value'];
  dataSource= new MatTableDataSource<IElement>([]);
  @ViewChild(MatPaginator) paginator?: MatPaginator;

  pageIndex ?: number;
  epageIndex = 0;
  numberOfRecords = 0;
  selected: IData | undefined;
  rawData?: IData[];
  fId?: string;

  form = new FormGroup({
      key: new FormControl('', Validators.required),
      value: new FormControl('', Validators.required),
  });
  constructor() {
    this.subscription1 = toObservable(this.dataStore.data).subscribe(value => {
      this.copyData(value);
    });
    this.subscription2 =  toObservable(this.dataStore.getCurrentSelected).subscribe((selected: IData | undefined) => {
      let b = false;
      if (this.selected === undefined) {
        b = selected !== undefined;
      }
      else if (selected === undefined || selected.id !== this.selected.id) {
        b = true;
      }
      if (b) {
        this.changeSelectedValue(selected);
        this.selected = selected;
        if (this.selected) {
          this.form.get('key')?.setValue(this.selected.key);
          this.form.get('value')?.setValue(this.selected.value);
        }
      }
    });
  }

  ngOnDestroy(): void {
    if (this.subscription1) {
      this.subscription1.unsubscribe()
    }
    if (this.subscription2) {
      this.subscription2.unsubscribe()
    }
    if (this.subscription3) {
      this.subscription3.unsubscribe()
    }
  }

  private changeSelectedValue(selected: IData | undefined): void {
    console.log('Change selected', selected);
    if (this.rawData) {
      let sid = -1;
      const d = new Array<IElement>();
      this.rawData.forEach((v, ix) => {
        const b = selected !== undefined && selected.id === v.id;
        if (b) {
          sid = ix + 1;
        }
        d.push({
          id: v.id,
          key: v.key,
          value: v.value,
          selected: b
        });
      });
      while ((d.length % this.numberPerPage) > 0) {
        d.push({
          id: '',
          key: '',
          value: '',
          selected: false
        })
      }
      if (sid >= 0) {
        let count = 0;
        let i = 0;
        while(count < sid) {
          count += this.numberPerPage;
          i++;
        }
        this.pageIndex = i - 1;
      }
      else if (this.pageIndex) {
        this.pageIndex = this.epageIndex;
      }
      this.setData(d);
    }
  }

  private copyData(value: IData[]) {
    this.rawData = value;
    const d = new Array<IElement>();
    value.forEach(v => {
      if (!this.fId) {
        this.fId = v.fid;
      }
      d.push({
        id: v.id,
        key: v.key,
        value: v.value,
        selected: false
      })
    });
    while ((d.length % this.numberPerPage) > 0) {
      d.push({
        id: '',
        key: '',
        value: '',
        selected: false
      })
    }
    this.setData(d);
    if (this.subscription3 === undefined && this.paginator) {
      this.dataSource.paginator = this.paginator;
      this.subscription3 = this.dataSource.paginator.page.subscribe(value => {
        console.log('Change page', value.pageIndex);
        this.epageIndex = value.pageIndex;
        this.dataStore.setSelected('');
      });
    }
  }

  private setData(d: IElement[]) {
    this.numberOfRecords = d.length;
    this.dataSource.data = d;
    console.log("Page index ", this.pageIndex, this.numberOfRecords);
  }

  selectRecord(event: Event, row: IElement) {
    if (row) {
      if (row.id !== '') {
        this.dataStore.setSelected(row.id);
      }
      else {
        this.wrappingService.addNew(this.fId? this.fId: '');
      }
    }
  }

  changeCurrentSelected(event: MouseEvent | FocusEvent) {
    const key = this.form.get('key')?.value;
    const value = this.form.get('value')?.value;
    if (this.selected && key !== undefined && key !== null && value !== undefined && value !== null) {
      this.wrappingService.changeData({
        id: this.selected.id,
        key: key,
        value: value,
        fid: this.fId? this.fId: ''
      });
    }
  }

}
