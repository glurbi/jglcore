#version 330 core

uniform mat4 mvpMatrix;
uniform vec4 color;

in vec3 position;
out vec4 gl_Color;

void main(void) 
{ 
	gl_Position = mvpMatrix * vec4(position, 1.0);
	gl_Color = color;
}
