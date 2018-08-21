uniform float iOvertoneVolume;
uniform float iGlobalBeatCount;
uniform float iActiveColor;
uniform float iA;
uniform float iB;
uniform float iC;
uniform float outt;

void main(void){
  vec2 uv = (gl_FragCoord.xy / iResolution.xy);
  uv.y=1.0-uv.y; //*sin(iDataArray[0])*2;

  vec4 t0 = texture2D(iChannel0,uv);

  vec4 fftw=texture2D(iFftWave, uv);

  vec4 c0 = texture2D(iCam0,uv);
  vec4 c1 = texture2D(iCam1,uv);
  vec4 c2 = texture2D(iCam2,uv);
  vec4 c3 = texture2D(iCam3,uv);
  vec4 c4 = texture2D(iCam4,uv);

  vec4 v0= texture2D(iVideo0, uv);
  vec4 v1= texture2D(iVideo1, uv);
  vec4 v2= texture2D(iVideo2, uv);
  vec4 v3= texture2D(iVideo3, uv);
  

const float tau = 6.28318530717958647692;
vec3 wave = vec3(0.0);
float width = v0.x*((iDataArray[0]*iDataArray[0]*iDataArray[0]*iDataArray[0])/100000000);
float n=0;
for (int i=0; i < 10; i++){
  n=1; //sin(iDataArray[0]);
  float sound = v0.x;

  float a = 0.1*float(i)*tau/float(n);
  vec3 phase = smoothstep(-1.0,1.5, vec3(cos(a), cos(a-tau/3.0), cos(a-tau*2.0/3.0)));
  wave += phase * smoothstep(width, 0.0, abs(uv.y - ((sound*0.9)+0.2)));

  //This shift of uv.x means our index into the sound data also 
  //moves along, examining a different part of the audio wave. 
  uv.x += 0.4/float(n);
  uv.y -= 0.05;
}
wave *= 10.0/float(10);

vec4 cf8=vec4(wave, 1);

    //color key

  vec4 bg=cf8;
  vec4 fg=v2;

  float maxrb = max( fg.r, fg.g);
  float k = clamp( (fg.b-maxrb)*90, 0.0, 1.0);

  float dg = fg.b;
  fg.b = min( fg.b, maxrb);//iDataArray[0]);
    fg += dg - fg.b;

    vec4 cf6=mix(fg, bg, k); //-sin(iDataArray[0]));

    //cf6.b=bg.g;
    //fg.b=fg.r*sin(iDataArray[0])*fg.g;
 
    
    
  gl_FragColor = cf6; //mix(v2, cf8, sin(iDataArray[0]));

}
