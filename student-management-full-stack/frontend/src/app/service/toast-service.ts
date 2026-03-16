import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface Toast {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private subject = new Subject<Toast>();
  toasts$ = this.subject.asObservable();

  success(msg: string) { this.emit('success', msg); }
  error(msg: string)   { this.emit('error', msg); }
  info(msg: string)    { this.emit('info', msg); }
  warning(msg: string) { this.emit('warning', msg); }

  private emit(type: Toast['type'], message: string) {
    this.subject.next({ id: crypto.randomUUID(), type, message });
  }
}