import { STOMP_WS_URL } from '$lib/config';
import { Client, type Frame, type Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

type Callback = (msg: string) => void;

class StompService {
  private client: Client | null = null;
  private subscribers: Callback[] = [];

  connect() {
    if (this.client) {
      return;
    }

    this.client = new Client({
      webSocketFactory: () => new SockJS(STOMP_WS_URL),
      reconnectDelay: 5000,
      debug: (str) => console.log('[STOMP]', str),
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.client.onConnect = (frame: Frame) => {
      console.log('Connected to STOMP', frame);

      this.client?.subscribe('/topic/notifications', (message: Message) => {
        this.notifySubscribers(message.body);
      });
    };

    this.client.onStompError = (frame) => {
      console.error('STOMP error', frame);
    };

    this.client.activate();
  }

  disconnect() {
    this.client?.deactivate();
    this.client = null;
  }

  subscribe(callback: Callback) {
    this.connect();
    this.subscribers.push(callback);
    return () => {
      this.subscribers = this.subscribers.filter((cb) => cb !== callback);
    };
  }

  private notifySubscribers(msg: string) {
    this.subscribers.forEach((cb) => cb(msg));
  }
}

const stompService = new StompService();
export default stompService;
