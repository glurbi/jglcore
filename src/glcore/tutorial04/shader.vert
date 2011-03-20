#version 330 core

uniform mat4 mvpMatrix;

in vec3 position;
in vec3 color;
out vec4 gl_Color;

void main(void) 
{ 
	gl_Position = mvpMatrix * vec4(position, 1.0);
	gl_Color = vec4(color, 1.0f);
}
