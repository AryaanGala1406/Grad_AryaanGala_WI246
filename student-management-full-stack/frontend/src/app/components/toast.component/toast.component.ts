import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Toast, ToastService } from '../../service/toast-service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  standalone: false,
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit, OnDestroy {
  toasts: Toast[] = [];
  private sub!: Subscription;

  constructor(private toastService: ToastService) {}

  ngOnInit() {
    this.sub = this.toastService.toasts$.subscribe(toast => {
      this.toasts.push(toast);
      setTimeout(() => this.remove(toast.id), 4000);
    });
  }

  remove(id: string) {
    this.toasts = this.toasts.filter(t => t.id !== id);
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}