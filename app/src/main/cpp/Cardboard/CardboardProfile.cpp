/* Copyright (C) 2016 Tcl Corporation Limited */
#include "CardboardProfile.h"

namespace android {

//static const Screen Nexus6({0.133f, 0.074, 0.004f});
static const Screen Nexus6({0.133f, 0.074, 0.004f});
//static float CardboardJun2014_Coeffiences[] = {0.441f, 0.156f};
//static float CardboardJun2014_Coeffiences[] = {0.072f, 0.053f};
    static float CardboardJun2014_Coeffiences[] = {0.336f, 0.553f};
static const Device CardboardJun2014 = {
	.lenses = {0.0639f, 0.0393f, 0.035f, Alignment::ALIGN_CENTER},
	.maxFOV = {20.0f, 20.0f, 20.0f, 20.0f},
	.distortion = {.coef = CardboardJun2014_Coeffiences, .coefCount = 2},
	.inverse = {.coef = CardboardJun2014_Coeffiences, .coefCount = 2}};

CardboardProfile::CardboardProfile()
	:	mScreen(Nexus6),
	 	mDevice(CardboardJun2014) {}

CardboardProfile::~CardboardProfile() {

}

void CardboardProfile::distort(float r, float& ret) {
	ret = mDevice.distortion.distort(r);
}

void CardboardProfile::distortInv(float r, float& ret) {
	ret = mDevice.distortion.distortInv(r);
}

void CardboardProfile::setDistortionParams(float k1, float k2, float k3) {
	mDevice.distortion.coefCount = 3;
	mDevice.distortion.coef[0] = k1 * 10.0f;
	mDevice.distortion.coef[1] = k2 * 10.0f;
	mDevice.distortion.coef[2] = k3 * 10.0f;
    mDevice.distortion.coef[3] = 10.0F;
    mDevice.distortion.coef[4] = 10.0F;
    mDevice.distortion.coef[5] = 10.0F;
    mDevice.distortion.coef[6] = 10.0F;
    mDevice.distortion.coef[7] = 10.0F;
    mDevice.distortion.coef[8] = 10.0F;
    mDevice.distortion.coef[9] = 10.0F;

	mDevice.inverse.coefCount = 3;
	mDevice.inverse.coef[0] = k1 * 10.0f;
	mDevice.inverse.coef[1] = k2 * 10.0f;
	mDevice.inverse.coef[2] = k3 * 10.0f;
    mDevice.inverse.coef[3] = 10.0F;
    mDevice.inverse.coef[4] = 10.0F;
    mDevice.inverse.coef[5] = 10.0F;
    mDevice.inverse.coef[6] = 10.0F;
    mDevice.inverse.coef[7] = 10.0F;
    mDevice.inverse.coef[8] = 10.0F;
    mDevice.inverse.coef[9] = 10.0F;
}

}
