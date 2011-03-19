#version 330 core

in vec4 gl_Color;
out vec4 fragColor;

void main(void) 
{ 
	fragColor = gl_Color; 
}
