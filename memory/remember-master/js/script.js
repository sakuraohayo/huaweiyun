// built from the tutorial
// https://www.youtube.com/watch?v=dhuigO4A7RY

// However getting weird fract on OSX
// machines with certian video cards?

const vShader = document.getElementById("vertexShader").textContent;
const fShader = document.getElementById("fragmentShader").textContent;
/**
                                                                       *
                                                                       */
const clock = new THREE.Clock();
const scene = new THREE.Scene();
const camera = new THREE.OrthographicCamera(-1, 1, 1, -1, .1, 10);
const renderer = new THREE.WebGLRenderer();
// append canvas element to document
document.body.appendChild(renderer.domElement);
// set canvas size and resize event handler

/**
*
*/
const geometry = new THREE.PlaneGeometry(2, 2);
const uniforms = {
  u_time: { value: 0.0 },
  u_mouse: { value: { x: 0.0, y: 0.0 } },
  u_resolution: { value: { x: 0, y: 0 } } };


const material = new THREE.ShaderMaterial({
  uniforms: uniforms,
  vertexShader: vShader,
  fragmentShader: fShader });

const plane = new THREE.Mesh(geometry, material);
scene.add(plane);

camera.position.z = 1;


onWindowResize();
if ('ontouchstart' in window) {
  document.addEventListener('touchmove', move);
} else {
  window.addEventListener('resize', onWindowResize, false);
  document.addEventListener('mousemove', move);
}

function move(evt) {
  const x = evt.touches ? touches[0].clientX : evt.clientX;
  const y = evt.touches ? touches[0].clientY : evt.clientY;
  uniforms.u_mouse.value.x = x;
  uniforms.u_mouse.value.y = y;
}
animate();
/**
           *
           */
function onWindowResize(event) {
  const aspectRatio = window.innerWidth / window.innerHeight;
  let width, height;
  if (aspectRatio >= 1) {
    width = 1;
    height = window.innerHeight / window.innerWidth * width;

  } else {
    width = aspectRatio;
    height = 1;
  }
  camera.left = -width;
  camera.right = width;
  camera.top = height;
  camera.bottom = -height;
  camera.updateProjectionMatrix();
  renderer.setSize(window.innerWidth, window.innerHeight);
  uniforms.u_resolution.value.x = window.innerWidth;
  uniforms.u_resolution.value.y = window.innerHeight;
}

function animate() {
  requestAnimationFrame(animate);
  uniforms.u_time.value = clock.getElapsedTime();
  //uniforms.u_time.value += clock.getDelta();
  renderer.render(scene, camera);
}