#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform float GameTime;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float dissolve; // Passed in via Callback
uniform int highlight;
uniform int red;
uniform vec3 blockpos;
uniform vec3 firstpos;
uniform vec3 secondpos;

in vec3 vertexPosition;
in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

void main() {
    //convert gametime to seconds (roughly)
    float time = GameTime * 1200;

    vec3 pixelposition = floor(vertexPosition * 8.0) / 8.0;
    vec3 worldpos = blockpos + pixelposition.xyz;
    vec4 texcolor = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    vec4 color = texcolor;
    vec3 firstposc = firstpos + 0.51; //center in block
    vec3 secondposc = secondpos + 0.5;

    //find place in between first and second pos
    float firstToSecond = length(secondposc - firstposc);
    float place = 0.0;
    if (firstToSecond > 0.5) {
        float placeFromFirst = length(worldpos - firstposc) / firstToSecond;
        float placeFromSecond = length(worldpos - secondposc) / firstToSecond;
        place = (placeFromFirst + (1.0 - placeFromSecond)) / 2.0;
    } //else only one block

    //find 2d texture coordinate for noise texture based on world position
    vec2 maskcoord = vec2(worldpos.y, worldpos.z);
    if (abs(normal.y) > 0.5)
        maskcoord = vec2(worldpos.x, worldpos.z);
    if (abs(normal.z) > 0.5)
        maskcoord = vec2(worldpos.x, worldpos.y);

    maskcoord /= 20.0;
    vec4 maskColor = texture(Sampler2, maskcoord);
    float maskgs = maskColor.r;

    color.rgb *= vertexColor.rgb;

    //desaturate
    color.rgb *= vec3(0.8);

    //add blueish hue
    color.rgb += vec3(-0.1, 0.0, 0.2);

    //add pulsing blue
    float pulse = (sin(time * 4.0) + 1.0) / 2.0;
    color.rgb += 0.4 * vec3(-0.5, 0.2, 0.6) * pulse;

    //add diagonal highlights
    float diagTime = mod(time / 2.0, 1.4) - 0.2;
    float diag = smoothstep(diagTime - 0.2, diagTime, place) - smoothstep(diagTime, diagTime + 0.2, place);
    color.rgb += 0.2 * diag * vec3(0.0, 0.2, 0.4);

    float diagTime2 = mod(time / 3.5, 1.4) - 0.2;
    float diag2 = smoothstep(diagTime2 - 0.2, diagTime2, place) - smoothstep(diagTime2, diagTime2 + 0.2, place);
    color.rgb += 0.2 * diag2 * vec3(0.0, 0.4, 0.8);

    //add flat shading
//    if (abs(normal.x) > 0.5)
//        color.rgb -= 0.0;
    if (abs(normal.y) > 0.5)
        color.rgb += 0.05;
    if (abs(normal.z) > 0.5)
        color.rgb -= 0.05;


    if (highlight == 1 && dissolve == 0.0) {
        color.rgb += vec3(0.0, 0.1, -0.2);
    }

    if (red == 1) {
        color.rgb += vec3(0.4, -0.3, -0.5);
    }

    color.r = max(0, min(1, color.r));
    color.g = max(0, min(1, color.g));
    color.b = max(0, min(1, color.b));

    if (maskgs * 0.3 + place * 0.7 <= dissolve)
    	fragColor = vec4(texcolor.rgb, 0.0);
    else fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
