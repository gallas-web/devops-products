import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { NzMessageService } from 'ng-zorro-antd/message';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private message: NzMessageService) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler) {
    const token = localStorage.getItem('ecommerce_token');
    const authReq = token
      ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : req;

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        let msg = 'Une erreur est survenue';
        if (error.error?.message) msg = error.error.message;
        else if (error.status === 0) msg = 'Impossible de contacter le serveur';
        else if (error.status === 404) msg = 'Ressource introuvable';
        else if (error.status === 409) msg = error.error?.message || 'Conflit de données';
        else if (error.status === 400) msg = error.error?.message || 'Données invalides';
        this.message.error(msg);
        return throwError(() => error);
      })
    );
  }
}
