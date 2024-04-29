/* Copyright (C) 2016 Tcl Corporation Limited */
#ifndef ANDROID_VRTYPES_H
#define ANDROID_VRTYPES_H

#include <stdint.h>
#include <math.h>

typedef struct {
	float width;
	float height;
	float border;
} Screen;

enum Alignment{
	ALIGN_TOP	 = -1,
	ALIGN_CENTER = 0,
	ALIGN_BOTTOM = 1
};

typedef struct {
	float separation;
	float offset;
	float screenDistance;
	Alignment alignment;
} Lenses;

typedef struct {
	float outer;
	float inner;
	float upper;
	float lower;
} MaxFOV;

typedef struct Distortion_{
	float* coef;
	size_t coefCount;

	float distort(float r) const {
		float r2 = r * r;
		float ret = 0;
		for (int j=coefCount-1; j>=0; j--) {
			ret = r2 * (ret + coef[j]);
		}
		return (ret + 1) * r;
	}

	float distortInv(float radius) const {
		float r0 = 0.0f;
		float r1 = 1.0f;
		float dr0 = radius - distort(r0);
		while (fabs(r1 - r0) > 0.0001f) {
			float dr1 = radius - distort(r1);
			float r2 = r1 - dr1 * ((r1 - r0) / (dr1 - dr0));
			r0 = r1;
			r1 = r2;
			dr0 = dr1;
		}
		return r1;
	}

} Distortion;

typedef struct {
	Lenses lenses;
	MaxFOV maxFOV;
	Distortion distortion;
	Distortion inverse;
} Device;

typedef struct {
	float x;
	float y;
	float width;
	float height;
} Rectf;

#endif
