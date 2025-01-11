import {Component, inject, OnDestroy} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';
import {DataStore} from "../../store/data.store";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule, MatIconRegistry} from "@angular/material/icon";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DataService} from "../../services/data.service";
import {Subscription} from "rxjs";
import {toObservable} from "@angular/core/rxjs-interop";
import {IData} from "../../store/store.definitions";
import {WrappingService} from "../../services/wrapping.service";

@Component({
  selector: 'app-toolbar',
  imports: [MatToolbarModule, MatIconModule, MatTooltipModule],
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.scss'
})
export class ToolbarComponent implements OnDestroy {
  readonly datastore = inject(DataStore);
  readonly wrappingService= inject(WrappingService);
  readonly iconRegistry=  inject(MatIconRegistry);
  hasSelected = false;
  selected?: IData | undefined;
  subscription?: Subscription;

  constructor(readonly sanitizer: DomSanitizer) {
    this.iconRegistry
        .addSvgIcon('add', sanitizer.bypassSecurityTrustResourceUrl('add.svg'))
        .addSvgIcon('remove', sanitizer.bypassSecurityTrustResourceUrl('remove.svg'))
        .addSvgIcon('upload', sanitizer.bypassSecurityTrustResourceUrl('upload.svg'));
    this.subscription = toObservable(this.datastore.getCurrentSelected).subscribe((selected: IData | undefined) => {
      this.selected = selected;
      this.hasSelected = (selected !== undefined);
    });
  }

  addRecord(event: MouseEvent | KeyboardEvent) {
    this.wrappingService.addNew();
  }

  deleteRecord(event: MouseEvent | KeyboardEvent) {
    if (this.selected !== undefined) {
      this.wrappingService.deleteRecord(this.selected);
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
