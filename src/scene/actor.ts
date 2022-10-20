import { Sprites, Textures } from "../assets";
import { Game } from "../game";

export interface ActorOptions {
  color?: string;
  texture?: Textures;
  sprite?: Sprites;
  x: number;
  y: number;
  width: number;
  height: number;
}

export class Actor {
  protected text: string;
  private color: string;
  private texture?: Textures;
  private sprite?: Sprites;
  private x: number;
  private y: number;
  private width: number;
  private height: number;
  private storedEvents: Map<string, Function>;
  private mouseInside: boolean;

  constructor(actorOptions: ActorOptions) {
    this.storedEvents = new Map<string, Function>();
    this.color = actorOptions.color;
    this.texture = actorOptions.texture;
    this.sprite = actorOptions.sprite;
    this.x = actorOptions.x;
    this.y = actorOptions.y;
    this.width = actorOptions.width;
    this.height = actorOptions.height;

    this.on("mouse_move", (options) => {
      if (this.insideActor(options.x, options.y)) {
        if (!this.mouseInside) {
          this.call("mouse_enter");
          this.mouseInside = true;
        }
      } else {
        if (this.mouseInside) {
          this.call("mouse_exit");
        }
        this.mouseInside = false;
      }
    });
  }

  public draw() {
    Game.drawImageFromActor(this);
    //TODO: Allow user to change where the text is drawn...
    if (this.text) {
      const metrics = Game.getCanvasContext().measureText(this.text);
      let textHeight =
        metrics.actualBoundingBoxAscent + metrics.actualBoundingBoxDescent;

      Game.drawText({
        text: this.text,
        x: this.x + this.width / 2 - metrics.width / 2,
        y: this.y + this.height / 2 + textHeight / 2,
        color: "black",
      });
    }
  }

  public onCreated() {}

  public call(eventName: string, options?) {
    if (this.storedEvents.has(eventName)) {
      //Call the stored callback function
      this.storedEvents.get(eventName)(options);
    }
  }

  public on(eventName: string, callback: (options) => void) {
    this.storedEvents.set(eventName, callback);
  }

  public insideActor(x: number, y: number): boolean {
    if (x >= this.x && x <= this.x + this.width) {
      //console.log("in x-bounds");
      if (y >= this.y && y <= this.y + this.height) {
        //console.log("in y-bounds");
        return true;
      }
    }
    return false;
  }

  public setTexture(texture: Textures) {
    this.texture = texture;
  }

  public addText(text: string) {
    this.text = text;
  }

  public getImage(): HTMLImageElement {
    return Game.getImage(this.texture);
  }

  public getX(): number {
    return this.x;
  }

  public getY(): number {
    return this.y;
  }

  public getWidth(): number {
    return this.width;
  }

  public getHeight(): number {
    return this.height;
  }

  public getColor(): string {
    return this.color;
  }

  public getSprite() {
    return this.sprite;
  }

  public getTexture() {
    return this.texture;
  }
}
