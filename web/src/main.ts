const frameEl = document.getElementById('frame') as HTMLImageElement | null;
const fpsEl = document.getElementById('fps');
const resEl = document.getElementById('res');

// In a real app, this would be received via WebSocket/HTTP; here we use a global or fallback
// eslint-disable-next-line @typescript-eslint/no-explicit-any
const globalObj: any = window as any;
const sample = globalObj.sampleFrame as string | undefined;

if (frameEl) {
  if (sample) frameEl.src = sample;
  frameEl.onload = () => {
    if (resEl) resEl.textContent = `${frameEl.naturalWidth}x${frameEl.naturalHeight}`;
  };
}

let last = performance.now();
let frames = 0;
function tick() {
  const now = performance.now();
  frames++;
  if (now - last >= 1000) {
    const fps = frames;
    if (fpsEl) fpsEl.textContent = String(fps);
    frames = 0;
    last = now;
  }
  requestAnimationFrame(tick);
}
requestAnimationFrame(tick);
