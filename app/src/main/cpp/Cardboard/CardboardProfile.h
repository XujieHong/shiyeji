/* Copyright (C) 2016 Tcl Corporation Limited */
#ifndef ANDROID_CARDBOARDPROFILE_H
#define ANDROID_CARDBOARDPROFILE_H

#include <stdint.h>
#include <math.h>
#include "vr_types.h"

namespace android {

class CardboardProfile {
public:
	CardboardProfile();
	~CardboardProfile();

	void distort(float r, float& ret);
	void distortInv(float radius, float& ret);
	void setDistortionParams(float k1, float k2, float k3);

private:
	Screen mScreen;
	Device mDevice;
};
}
#endif
